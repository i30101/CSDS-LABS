# Author: Andrew Kim
# Version: 1.0.0
# Since: 11 April 2024
# Polynomial solver in Python


# import external libraries
import numpy as np
import matplotlib.pyplot as plt


# minimum and maximum values of graph
X_MIN = -10
X_MAX = 10
Y_MIN = -10
Y_MAX = 10

# desired resolution
RESOLUTION = 200

# x-values to be used in graph
X = np.linspace(X_MIN, X_MAX, RESOLUTION)

# set dimensions of figure on screen
plt.figure(figsize=(10, 7))







# adds vertical lines to graph
# x is the x-value where the vertical line should be added
def vert_line(x_value: float):
    plt.axvline(x = x_value, color='b', linewidth=1)


class Polynomial:
    def __init__(self, c: list):
        self.coefficients = c
        self.degree = len(c) - 1
        self.name = self.get_name()


    # returns string with name of polynomial in standard form
    def get_name(self) -> str:
        n = ""
        for i, coefficient in enumerate(self.coefficients):
            if coefficient != 0:
                if i != 0:
                    n += " + "
                if coefficient != 1:
                    n += coefficient
                n += f"x^{self.degree - i}"
        return n
    
    # returns y value of function given x value
    def f(self, x):
        value = 0
        for i, coefficient in enumerate(self.coefficients):
            value += (coefficient) * (x ** (self.degree - i))
        return value
    
    # plots graph of function 
    def plot(self):
        y_values = self.f(X)
        plt.plot(X, y_values, label=self.name)

        



x_squared = Polynomial([1, 0, 0])
x_squared.plot()


vert_line(2)



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


show()