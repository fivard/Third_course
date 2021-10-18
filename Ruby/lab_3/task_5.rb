$N = 100
$c = 12

def calculate_y(x)
  (x**(1/4) - (1/x)**(1/4))**(-$N) +
    (x + 1) / (x**2 - 4*x + 3*$N) +
    ((36*x*$c*$N)**(1/4) / ((x + $c*$N + 1) / (x + 5)))
end

def calculate_z(x)
  ((Math.tan(2*x) * Math.cos(2*x)**(-1) - Math.tan(2*$c + x) * Math.cos(2*x)**(-2)) /
    (Math.cos(2*x)**(-1) + Math.cos(3*x)**(-2)))
  +
  (1 + (Math.cos($N * x))**(1/$c)) /
    (2*x + Math.sin(3*x)**(2))
end

