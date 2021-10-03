import sympy as sp
import numpy as np
import matplotlib.pyplot as plt

# x = -0.57860


def plot_func(y, start=-1, stop=0, steps=100):
    func = sp.lambdify(_x, y)
    xx = np.linspace(start, stop, steps)
    yy = func(xx)

    x_intersects = [float(r) for r in sp.solve(y, _x) if r.as_real_imag()[1] == 0]
    x_intersects = [r for r in x_intersects if start <= r <= stop]
    plt.axhline(0, color='black')
    if start <= 0 <= stop:
        plt.axvline(0, color='black', ymax=1)
    plt.plot(xx, yy)
    plt.plot(x_intersects, [0] * len(x_intersects), color='red', marker='o')
    plt.show()


_x = sp.Symbol('x', real=True)


# ----------------- Newton's method -------------


y = _x ** 3 - 10 * _x ** 2 + 44 * _x + 29
y_dx = y.diff()

func = sp.lambdify(_x, y)
func_dx = sp.lambdify(_x, y_dx)

eps = 1e-4

plot_func(y)

xx = [-0.6]
while abs(func(xx[-1])) > eps:
    x = xx[-1] - func(xx[-1]) / func_dx(xx[-1])
    xx.append(x)

print(xx)

# ------------------ Simple Iteration Method ------------------
y = -1 * _x ** 3 / 44 + 10 / 44 * _x ** 2 - 29 / 44
func_fi = sp.lambdify(_x, y)

xx = [-0.6]
while abs(func(xx[-1])) > eps:
    x = func_fi(xx[-1])
    xx.append(x)

print(xx)
