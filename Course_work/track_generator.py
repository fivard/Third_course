import random

import numpy as np
import math
from scipy import interpolate

import matplotlib.pyplot as plt

MAX_HEIGHT = 10


def generate_track(start_position, end_position, count_vertexes):
    x = np.linspace(start_position, end_position, count_vertexes)
    y = [MAX_HEIGHT if i == 0 else random.randint(0, MAX_HEIGHT) for i in range(count_vertexes)]
    vertexes = np.array(list(zip(x, y)))

    ctr = np.array(vertexes)
    x = ctr[:, 0]
    y = ctr[:, 1]
    length = len(x)

    t = np.linspace(0, 1, length - 2, endpoint=True)
    t = np.append([0, 0, 0], t)
    t = np.append(t, [1, 1, 1])

    tck = [t, [x, y], 3]
    u3 = np.linspace(0, 1, (max(length * 2, 200)), endpoint=True)
    out = interpolate.splev(u3, tck)\

    return list(zip(out[0], out[1]))


def generate_track_np(start_position, end_position, count_vertexes):
    x = np.linspace(start_position, end_position, count_vertexes)
    y = [MAX_HEIGHT if i == 0 else random.randint(0, MAX_HEIGHT) for i in range(count_vertexes)]

    z = np.polyfit(x, y, 10)
    poly = np.poly1d(z)

    new_x = np.linspace(start_position, end_position, 200)
    new_y = [poly(i) for i in new_x]

    return poly, list(zip(new_x, new_y))
