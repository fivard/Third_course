import numpy as np
np.set_printoptions(precision=3)
given_matrix = np.array([[11,     7,      3,      7],
                         [7,      10,    -1,     4],
                         [3,      -1,     16,     -7],
                         [7,      4,      -7,     15]])
eps = 0.01


def find_max_own_value(matrix):
    current_ly = 0
    prev_ly = -1

    x = np.array([1, 1, 1, 1])

    while abs(current_ly - prev_ly) > eps:
        prev_ly = current_ly
        e = np.divide(x, np.linalg.norm(x, ord=np.inf))
        # print("e = ", e)
        x = np.dot(matrix, e)
        # print("x = ", x)
        current_ly = np.dot(x, e) / np.dot(e, e)
        # print("current_ly = ", current_ly)

    return current_ly


ly_a_max = find_max_own_value(given_matrix)
b = np.dot(ly_a_max, np.eye(4)) - given_matrix
ly_b_max = find_max_own_value(b)
ly_a_min = ly_a_max - ly_b_max

print("max = ", ly_a_max)
print("min = ", ly_a_min)
