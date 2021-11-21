import numpy as np
np.set_printoptions(precision=5)
given_matrix = np.matrix('7,     11,      3,      7,      2;'
                         '11,     10,    -1,     4,       2;'
                         '3,      -1,     16,     -7,     -3;'
                         '7,      4,      -7,     15,     5')

vector = np.matrix('2, 2, -3, 5')


def generate_m(matrix, iteration):
    m = np.eye(4)
    for i in range(iteration, 4):
        if i == iteration:
            m[i, iteration] = 1 / matrix[iteration, iteration]
        else:
            m[i, iteration] = -matrix[i, iteration] / matrix[iteration, iteration]

    return m


def generate_p(i, j):
    e = np.eye(4)
    e[:, [i, j]] = e[:, [j, i]]
    return e


def find_index_max_in_a_row(matrix, iteration):
    return np.matrix.argmax(matrix, axis=0).item(iteration)


def gauss_method(matrix):
    for i in range(4):
        print(f"\n-----------------ITERATION {i}----------------")
        row_maximum_index = find_index_max_in_a_row(matrix, i)
        print(f"\nIndex of the biggest number is {row_maximum_index} in {i}'s row\n")
        p = generate_p(i, row_maximum_index)
        print(f"\nP is :\n{p}")
        matrix = np.matmul(p, matrix)
        print(f"\nP * A =\n{matrix}\n")
        m = generate_m(matrix, i)
        print(f"\nM is :\n{m}")
        matrix = np.matmul(m, matrix)
        print(f"\nM * A is :\n{matrix}")

    x4 = matrix[3, 4]
    x3 = matrix[2, 4] - matrix[2, 3] * x4
    x2 = matrix[1, 4] - matrix[1, 3] * x4 - matrix[1, 2] * x3
    x1 = matrix[0, 4] - matrix[0, 3] * x4 - matrix[0, 2] * x3 - matrix[0, 1] * x2
    print('\nSolution: x1 = %0.5f, x2 = %0.5f, x3 = %0.5f and x4 = %0.5f\n' % (x1, x2, x3, x4))


gauss_method(given_matrix)
