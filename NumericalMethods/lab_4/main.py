import math

import numpy as np
import matplotlib.pyplot as plt

a = -5
b = 5
N = 9
step = (b - a) / N


def test_function1(x):
    return 4.0 ** x


def test_function2(x):
    return math.cos(x)


def get_chebyshev_nulls_points():
    return np.array([(a + b) / 2 + (b - a) * math.cos((2 * k + 1) * math.pi / (2 * N)) / 2 for k in range(N)])


def get_equally_distant_points():
    return np.array(np.arange(-step * (N // 2), step * (N // 2) + 1, step))


def calculate_separated_difference(x_array, y_array, i, j):
    return (y_array[j+1] - y_array[j]) / (x_array[i+j+1] - x_array[j])


def generate_template_polynomial():
    koef_template = "fx.$"
    bracket_template = "(x-x$)"
    polynomial = ""
    for i in range(N):
        polynomial += koef_template.replace("$", str(i))
        for j in range(i):
            polynomial += "*" + bracket_template.replace("$", str(j))
        if i < N-1:
            polynomial += " + "
    return polynomial


def newton_polynomial(x_points, y_points):
    polynomial = generate_template_polynomial()
    # print(polynomial)
    koef = [y_points[0]]
    prev_separated_differences = y_points
    for i in range(N-1):
        separated_differences = []
        for j in range(N-i-1):
            separated_differences.append(calculate_separated_difference(x_points, prev_separated_differences, i, j))
        koef.append(separated_differences[0])
        prev_separated_differences = separated_differences
    for i in range(N):
        polynomial = polynomial.replace(f"fx.{i}", str(koef[i]))
        polynomial = polynomial.replace(f"x{i}", str(x_points[i]))
    print(polynomial)
    return polynomial


if __name__ == "__main__":
    function = test_function2
    chebyshev_points = get_chebyshev_nulls_points()
    eq_d_points = get_equally_distant_points()
    chebyshev_values = np.array([function(x) for x in chebyshev_points])
    eq_d_values = np.array([function(x) for x in eq_d_points])

    chebyshev_poly = newton_polynomial(chebyshev_points, chebyshev_values)
    eq_d_poly = newton_polynomial(eq_d_points, eq_d_values)

    x_new = np.arange(a, b, 0.1)
    chebyshev_y = []
    eq_d_y = []
    for x in x_new:
        chebyshev_y.append(eval(chebyshev_poly))
        eq_d_y.append(eval(eq_d_poly))

    fig, axs = plt.subplots(1, figsize=(8, 8))
    axs.plot(x_new, eq_d_y, 'b', label='Equally distant')
    axs.plot(x_new, chebyshev_y, 'g', label='Chebyshev')
    axs.plot(x_new, np.array([function(x) for x in x_new]), 'r', label='Function')
    fig.suptitle('Newton Polynomials')
    plt.show()
