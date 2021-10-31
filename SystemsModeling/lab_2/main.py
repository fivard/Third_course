import numpy as np
import matplotlib.pyplot as plt
from matplotlib import image

X, Y = image.imread('x1.bmp'), image.imread('y4.bmp')


def moore_penrose_method(A, sigma0=1, e=1e-5):
    A = np.array(A, dtype=float)
    AT = A.T
    AAT = np.dot(A, AT)
    E = np.eye(A.shape[0])
    prev = np.dot(AT, np.linalg.inv(AAT + sigma0 * E))
    while True:
        sigma_k = sigma0 / 2
        current = np.dot(AT, np.linalg.inv(AAT + sigma_k * E))
        if np.linalg.norm(current - prev) < e:
            break
        prev = current
    return current


def greville_method(M):
    M = np.array(M, dtype=float)
    ai = M[0:1]
    if np.count_nonzero(ai[0]) == 0:
        res = np.zeros_like(ai.T)
    else:
        res = ai.T / np.dot(ai, ai.T)

    n = M.shape[0]
    for i in range(1, n):
        z_a = np.eye(res.shape[0]) - np.dot(res, M[:i])
        r_a = np.dot(res, res.T)
        ai = M[i:i + 1]

        dot_product = np.dot(np.dot(ai, z_a), ai.T)
        if np.count_nonzero(dot_product) != 1:
            a_inv = np.dot(r_a, ai.T) / (1 + np.dot(np.dot(ai, r_a), ai.T))
        else:
            a_inv = np.dot(z_a, ai.T) / dot_product

        res -= np.dot(a_inv, np.dot(ai, res))
        res = np.concatenate((res, a_inv), axis=1)
    return res


def asserting_methods(A_plus_matrix):
    assert np.dot(np.dot(X, A_plus_matrix), X).all() == X.all()
    assert np.dot(np.dot(A_plus_matrix, X), A_plus_matrix).all() == A_plus_matrix.all()
    assert np.allclose(np.dot(X, A_plus_matrix), np.dot(X, A_plus_matrix).T)
    assert np.allclose(np.dot(A_plus_matrix, X), np.dot(A_plus_matrix, X).T)
    print("Asserted\n")


A_plus_moore = moore_penrose_method(X)
A_plus_greville = moore_penrose_method(X)

A_moore = np.dot(Y, A_plus_moore)
A_greville = np.dot(Y, A_plus_greville)

print("Asserting Moore Penrose's method...")
asserting_methods(A_plus_moore)
print("Asserting Greville's method...")
asserting_methods(A_plus_greville)

plt.imshow(Y, cmap='gray')
plt.show()
plt.imshow(np.dot(A_moore, X), cmap='gray')
plt.show()
plt.imshow(np.dot(A_greville, X), cmap='gray')
plt.show()
