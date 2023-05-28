import random

import numpy as np


def generate_track_np(start_position, end_position, count_vertexes, max_height):
    max_height = max_height * 0.7
    x = np.linspace(start_position, end_position, count_vertexes, endpoint=True)
    y = [max_height if i == 0 or i == end_position else random.randint(max_height*0.2, max_height) for i in range(count_vertexes)]

    z = np.polyfit(x, y, 10)
    poly = np.poly1d(z)

    new_x = np.linspace(start_position, end_position, 200)
    new_y = [poly(i) for i in new_x]

    return poly, list(zip(new_x, new_y))
