# Author: Andrew Kim
# Version: 1.0.0
# Since: 11 April 2024
# Polynomial solver in Python


# import external libraries
import numpy as np
import matplotlib.pyplot as plt
import random


# minimum and maximum values of graph
X_MIN = -100
X_MAX = 100
Y_MIN = -100
Y_MAX = 100

# desired resolution
# the number of increments the x-range should be divided into
RESOLUTION = 200

# comparison to 0
ALMOST_ZERO = 0.0000000000001

# x-values to be used in graph
X = np.linspace(X_MIN, X_MAX, RESOLUTION)

# set dimensions of figure on screen
plt.figure(figsize=(10, 7))



# helper method: two numbers are different signs
def same_signs(a, b) -> bool:
    return (a > 0) == (b > 0)


# helper method: finds average of two numbers
def average(a, b) -> float:
    return (a + b) / 2


# adds vertical lines to graph
# x is the x-value where the vertical line should be added
def vert_line(x_value: float):
    plt.axvline(x = x_value, color='b', linewidth=1)


class Polynomial:
    def __init__(self, c: list):
        self.coefficients = c
        self.degree = len(c) - 1
        self.name = self.get_name()
        self.y_values = self.f(X)
        self.roots = self.find_zeroes()


    # returns string with name of polynomial in standard form
    def get_name(self) -> str:
        n = ""
        for i, coefficient in enumerate(self.coefficients):
            # skip term altogether if coefficient is zero
            if coefficient != 0:
                if i != 0:
                    n += " " if coefficient < 0 else " +"
                # skip coefficient if it is 1
                n += str(coefficient) if coefficient != 1 else ""
                # add x with power
                power = self.degree - i
                if power == 1:
                    n += "x"
                elif power != 0:
                    n += f"x^{power}"
        return n
    

    # returns y value of function given x value
    def f(self, x):
        value = 0
        for i, coefficient in enumerate(self.coefficients):
            value += (coefficient) * (x ** (self.degree - i))
        return value
    

    # returns whether x-value is root or not
    def is_root(self, x):
        return self.f(x) == 0
    

    # finds ranges where there must be a root
    # gives indeces where y-value differs from that of the next index
    def root_indeces(self) -> list:
        indeces = []
        for i, x in enumerate(X[:-1]):
            if not same_signs(self.f(x), self.f(X[i + 1])):
                indeces.append(i)
        return indeces


    # finds zeroes of polynomial
    # returns list with x-values
    def find_zeroes(self) -> list:
        zeroes = []
        for i in self.root_indeces():
            lower = X[i]
            upper = X[i + 1]
            mid = average(lower, upper)
            while abs(lower - upper) > ALMOST_ZERO:
                vert_line(mid)
                if same_signs(self.f(lower), self.f(mid)):
                    lower = mid
                elif same_signs(self.f(mid), self.f(upper)):
                    upper = mid
                else:
                    print("error")
                mid = average(lower, upper)
            zeroes.append(mid)
        return zeroes
            

    # plots graph of function 
    def plot(self):
        plt.plot(X, self.y_values, label=self.name)

    
    # finds accuracy of polynomial
    def find_accuracy(self):
        # find actual roots using numpy
        actual = [root.real for root in np.roots(self.coefficients) if np.isreal(root)]

        # print error if number of predicted and actual roots are different
        if len(self.roots) != len(actual):
            print("error: different number of roots found")
            print(self.roots)
            print(actual)
            return
        
        # calculate error of predictions
        error_sum = 0
        for i, actual_root in enumerate(actual):
            error_sum += abs((self.roots[i] - actual_root) / actual_root)
        error = error_sum / len(actual)
        return 100 * (1 - error)

        



pol = Polynomial([1, 2, -5, 3, 4, -3, 4, -8])


def trials(runs: int):
    errors = []
    for i in range(runs):
        pol = Polynomial([random.randint(-20, 20) for j in range(random.randint(3, 10))])
        if len(pol.find_zeroes()) != 0:
            errors.append(pol.find_accuracy())
    return sum(errors) / len(errors)


trials(100)

# shows graph with functions
def show():
    # add x-axis
    plt.axvline(x=0, color='k', linewidth=1)
    plt.axhline(y=0, color='k', linewidth=1)

    # set min and max of graph
    plt.xlim(X_MIN, X_MAX)
    plt.ylim(Y_MIN, Y_MAX)

    # add labels to graph
    plt.xlabel("X-Axis")
    plt.ylabel("Y-Axis")
    plt.title("Polynomial Solver Visual Analysis")

    plt.legend()
    plt.show()

# show()