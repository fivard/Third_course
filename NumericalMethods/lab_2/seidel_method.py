import numpy as np

matrix = np.matrix('11,     7,      3,      7;'
                   '7,      10,     -1,     4;'
                   '3,      -1,     16,     -7;'
                   '7,      4,      -7,     15')

matrix_3 = np.matrix(f'{matrix.item(0, 0)},    {matrix.item(0, 1)},      {matrix.item(0, 2)};'
                     f'{matrix.item(1, 0)},    {matrix.item(1, 1)},      {matrix.item(1, 2)};'
                     f'{matrix.item(2, 0)},    {matrix.item(2, 1)},      {matrix.item(2, 2)}')

matrix_2 = np.matrix(f'{matrix.item(0, 0)},    {matrix.item(0, 1)};'
                     f'{matrix.item(1, 0)},    {matrix.item(1, 1)}')

vector = [2, 2, -3, 5]


def validation():
    return np.linalg.det(matrix) > 0 and np.linalg.det(matrix_2) > 0 \
           and np.linalg.det(matrix_3) > 0 and np.allclose(matrix, matrix.T)


print('Matrix is :\n', matrix)
if validation():
    print("Seidel's method converges")
else:
    print("Seidel's method doesn't converge")


f1 = lambda x1, x2, x3, x4: (vector[0] - (matrix.item(0, 1) * x2 + matrix.item(0, 2) * x3 + matrix.item(0, 3) * x4)) / matrix.item(0, 0)
f2 = lambda x1, x2, x3, x4: (vector[1] - (matrix.item(1, 0) * x1 + matrix.item(1, 2) * x3 + matrix.item(1, 3) * x4)) / matrix.item(1, 1)
f3 = lambda x1, x2, x3, x4: (vector[2] - (matrix.item(2, 0) * x1 + matrix.item(2, 1) * x2 + matrix.item(2, 3) * x4)) / matrix.item(2, 2)
f4 = lambda x1, x2, x3, x4: (vector[3] - (matrix.item(3, 0) * x1 + matrix.item(3, 1) * x2 + matrix.item(3, 2) * x3)) / matrix.item(3, 3)

x01, x02, x03, x04 = 0, 0, 0, 0
x11, x12, x13, x14 = 0, 0, 0, 0
count = 1

e = 0.001

print('\nCount\tx1\tx2\tx3\tx4\n')

condition = validation()

while condition:
    x11 = f1(x01, x02, x03, x04)
    x12 = f2(x11, x02, x03, x04)
    x13 = f3(x11, x12, x03, x04)
    x14 = f4(x11, x12, x13, x04)
    print('%d\t%0.4f\t%0.4f\t%0.4f\t%0.4f\n' % (count, x11, x12, x13, x14))
    e1, e2, e3, e4 = abs(x01 - x11), abs(x02 - x12), abs(x03 - x13), abs(x04 - x14)

    count += 1
    x01 = x11
    x02 = x12
    x03 = x13
    x04 = x14

    condition = max(e1, e2, e3, e4) > e

if validation():
    print('\nSolution: x1 = %0.3f, x2 = %0.3f, x3 = %0.3f and x4 = %0.3f\n' % (x11, x12, x13, x14))

matrix_inv = np.linalg.inv(matrix)
print("Number of conditionality is ", np.linalg.norm(matrix, np.inf) * np.linalg.norm(matrix_inv, np.inf))
