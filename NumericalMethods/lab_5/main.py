import numpy as np

with open('y4.txt') as file:
    data = np.array([line.split() for line in file.readlines()], float).T

c1 = 0.14
c2 = 0.3
c4 = 0.12
m1 = 12


def getA(b):
    return np.array([
        [0, 1, 0, 0, 0, 0],
        [-(c2 + c1) / m1, 0, c2 / m1, 0, 0, 0],
        [0, 0, 0, 1, 0, 0],
        [c2 / b[0], 0, -(c2 + b[1]) / b[0], 0, b[1] / b[0], 0],
        [0, 0, 0, 0, 0, 1],
        [0, 0, b[1] / b[2], 0, -(c4 + b[1]) / b[2], 0]
    ])


def calc_db(y, b):
    db0 = np.array([
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [- c2 / (b[0] ** 2), 0, (c2 + b[1]) / (b[0] ** 2), 0, -b[1] / (b[0] ** 2), 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0]
    ])

    db1 = np.array([
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, -1 / b[0], 0, 1 / b[0], 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 1 / b[2], 0, -1 / b[2], 0]
    ])

    db2 = np.array([
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0],
        [0, 0, -b[1] / (b[2]**2), 0, (c4 + b[1]) / (b[2]**2), 0]
    ])

    db0 = db0 @ y
    db1 = db1 @ y
    db2 = db2 @ y

    return np.array([db0, db1, db2]).T


def calc_f(y, b):
    return getA(b) @ y


b0 = np.array([21, 0.15, 11])
dt = 0.2
eps = 0.001
timestamps = np.linspace(0, 50, 251)


def integral():
    yy = np.zeros_like(data)
    yy[0] = data[0].copy()
    A = getA(b_curr)
    for i in range(1, len(timestamps)):
        y_prev = yy[i - 1]
        k1 = dt * (A @ y_prev)
        k2 = dt * (A @ y_prev + k1 / 2)
        k3 = dt * (A @ y_prev + k2 / 2)
        k4 = dt * (A @ y_prev + k3)
        y = y_prev + (k1 + 2 * k2 + 2 * k3 + k4) / 6
        yy[i] = y

    uu = np.zeros((len(timestamps), 6, 3))
    db = calc_db(yy.T, b_curr)
    for i in range(1, len(timestamps)):
        k1 = dt * (A @ uu[i - 1] + db[i - 1])
        k2 = dt * (A @ (uu[i - 1] + k1 / 2) + db[i - 1])
        k3 = dt * (A @ (uu[i - 1] + k2 / 2) + db[i - 1])
        k4 = dt * (A @ (uu[i - 1] + k3) + db[i - 1])
        u_next = uu[i - 1] + (k1 + 2 * k2 + 2 * k3 + k4) / 6
        uu[i] = u_next

    return [yy, uu]


bb = [b0]

while True:
    b_curr = bb[-1].copy()
    yy, uu = integral()

    du = np.linalg.inv((np.array([u.T @ u for u in uu]) * dt).sum(0))
    uY = (np.array([uu[i].T @ (data[i] - yy[i]) for i in range(len(timestamps))]) * dt).sum(0)
    db = du @ uY

    bb.append(b_curr + db)

    if np.abs(db).max() < eps:
        break

print(np.round(b_curr, 2))
