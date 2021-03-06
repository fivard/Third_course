import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from "@angular/forms";
import { Router } from '@angular/router';
import { OAuthService } from "angular-oauth2-oidc";
import { first, map, mergeMap } from "rxjs/operators";

import { DishReadWriteDto } from "../../shared/dto/dish";
import { MenuService, OrderService, AuthService, UserService } from "../../service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.sass']
})
export class MenuComponent implements OnInit {
  orderForm: FormGroup;
  dishes: DishReadWriteDto[];
  submitted = false;
  loading = false;
  errorMessage = null;
  successfullMessage = null;

  private readonly prices: Record<string, number> = {
    'Naggets': 8,
    'Diablo': 30,
    'Chicken': 10,
    'Margarita': 15,
    'Stripces':6,
    'Neapolitana':25,
    'Big Tasty':20,
    'Four cheeses':30,
    'Milkshake':10,
    'Chicken roll':20,
    'Coca-cola':5,
    'BigMac': 10
  };

  constructor(private readonly formBuilder: FormBuilder, private readonly router: Router,
    private readonly authService: AuthService, private readonly menuService: MenuService,
    private readonly orderService: OrderService, private readonly userService: UserService,
    private readonly oauthService: OAuthService) { }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      orderDishes: this.formBuilder.array([])
    });
    this.getAllDishes();
  }

  getAllDishes(): void {
    this.menuService.getAllDishes().subscribe(
      (dishes) => {
        this.dishes = dishes;

        this.dishes.forEach(dish => {
          const order = this.formBuilder.group({
            dishName: dish.name,
            isOrdered: false,
            amount: 0,
          });
          this.orderDishes.push(order);
        })
      }
    );
  }

  get orderDishes() {
    return this.orderForm.get('orderDishes') as FormArray;
  }

  onSubmit(): void {
    this.submitted = true;
    this.loading = true;

    const formData = this.orderForm.value.orderDishes;
    for (const [key, value] of Object.entries(formData)) {
      if (!value) {
        delete formData[key]
      }
    }

    const orderDishes = formData
      .filter(orderDish => orderDish.isOrdered)
      .map(dish => {
        return {
          dishName: dish.dishName,
          quantity: dish.amount
        }
      });

    const username = this.authService.identityClaims['preferred_username'];
    const order = {
      username,
      dishes: orderDishes as any[],
      status: 'PENDING',
    };

    const totalPrice = order.dishes.reduce((acc, dish) => acc + this.prices[dish.dishName] * dish.quantity, 0);

    this.userService.getUserInfo(username).pipe(
      map(user => {
        if (totalPrice > user.balance) {
          throw new Error('User does not have enough money to make this order');
        }
        return user.balance;
      }),
      mergeMap(oldBalance => this.userService.changeBalance(username, oldBalance - totalPrice)),
      mergeMap(() => this.orderService.checkout(order).pipe(
        first(),
      )),
    ).subscribe(
      (data) => {
        this.successfullMessage = 'Order was submitted successfully';
        this.errorMessage = null;
        this.router.navigate(['/orders']);
      },
      (error) => {
        this.successfullMessage = null;
        this.errorMessage = error;
        this.loading = false;
      },
    );
  }
}
