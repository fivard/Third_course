import numpy as np
from matplotlib import pyplot as plt


def get_A(n):
    res = np.zeros((n - 2, n - 2))
    for i in range(0, n - 2):
        cur = np.zeros(n - 2)
        for j in range(i - 1, i + 2):
            if j == i:
                cur[j] = 2 / 3
            elif 0 <= j < n - 2:
                cur[j] = 1 / 6
        res[i] = cur
    return res


def get_H(n):
    res = np.zeros((n - 2, n))
    for i in range(0, n - 2):
        cur = np.zeros(n)
        for j in range(i, i + 3):
            if j == i + 1:
                cur[j] = -2
            elif j < n:
                cur[j] = 1
        res[i] = cur
    return res


def get_m(n, f):
    A = get_A(n)
    H = get_H(n)
    return np.concatenate(([0], np.linalg.inv(A) @ H @ f, [0]))


def get_s(x, f):
    M = get_m(x.size, f) / 6
    s = lambda p, i: M[i - 1] * ((x[i] - p) ** 3) + M[i] * ((p - x[i - 1]) ** 3) + (f[i - 1] - M[i - 1]) * (
                x[i] - p) + (f[i] - M[i]) * (p - x[i - 1])
    return s


def get_L(x, f):
    def L(p, i):
        return (f[i - 1] * (p - x[i]    ) * (p - x[i + 1])) / 2 - \
               f[i]      * (p - x[i - 1]) * (p - x[i + 1]) + \
               (f[i + 1] * (p - x[i - 1]) * (p - x[i])) / 2

    return L


xx, step = np.linspace(-5, 5, 11, retstep=True)

assert step == 1
yy = np.random.randint(50, size=11)
for i in range(1, len(xx)):
    yy[i] = yy[i - 1] + np.random.randint(-10, 10)
print(yy)

s = get_s(xx, yy)
L = get_L(xx, yy)

plt.plot(xx, yy)
plt.show()

for i, (start, stop) in enumerate(zip(xx, xx[1:])):
    x_hat = np.linspace(start, stop, num=50)
    y_hat = [s(x, i + 1) for x in x_hat]
    plt.plot(x_hat, y_hat)
plt.show()

for i, (start, stop) in enumerate(zip(xx[::2], xx[2::2])):
    x_hat = np.linspace(start, stop, num=50)
    y_hat = [L(x, 2*i + 1) for x in x_hat]
    plt.plot(x_hat, y_hat)

plt.show()

