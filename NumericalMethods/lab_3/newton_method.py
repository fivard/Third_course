import math
import numpy as np
f1 = "math.sin(x-0.6) - y - 1.6"
f2 = "3*x - math.cos(y) - 0.9"

f1_diff_x = "math.cos(x-0.6)"
f1_diff_y = "-1"
f2_diff_x = "3"
f2_diff_y = "math.sin(x)"

eps = 0.0000001

z_vector = np.array([0, 0])
x_vector = np.array([0, 0])
step = 0

while np.linalg.norm(z_vector, np.inf) > eps or step == 0:
    x_vector = x_vector - z_vector
    # print(x_vector)
    x, y = x_vector[0], x_vector[1]
    # print(x, y)
    A = np.array([[eval(f1_diff_x), eval(f1_diff_y)],
                  [eval(f2_diff_x), eval(f2_diff_y)]])
    # print(A)
    F = np.array([eval(f1), eval(f2)])
    # print(F)
    z_vector = np.dot(np.linalg.inv(A), F)
    # print(z_vector)
    step += 1

x_vector = x_vector - z_vector
x, y = x_vector[0], x_vector[1]
print("Answer is [x, y] is [", f'{x:9.3f}', ",", f'{y:9.3f}', "]")
print(f"f1 = {eval(f1):9.5f}")
print(f"f2 = {eval(f2):9.5f}")
