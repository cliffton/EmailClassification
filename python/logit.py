
import numpy as np
import matplotlib.pyplot as plt
import math

plt.subplots_adjust(hspace=0.4)
t = np.arange(1, 2, 3)

# # log y axis
# plt.subplot(221)
# plt.semilogy(t, np.exp(-t/5.0))
# plt.title('semilogy')
# plt.grid(True)

# # log x axis
# plt.subplot(222)
# plt.semilogx(t, np.sin(2*np.pi*t))
# plt.title('semilogx')
# plt.grid(True)

# log x and y axis
# plt.subplot(223)
# import pdb; pdb.set_trace()
# plt.loglog([10,100,1000], [20,2000,2000], basex=10, basey=10)
# plt.plot([1,10,100], [1,50,100])
# plt.grid(True)
# plt.title('loglog base 2 on x')

# with errorbars: clip non-positive values
# ax = plt.subplot(224)
# ax.set_xscale("log", nonposx='clip')
# ax.set_yscale("log", nonposy='clip')

# x = 10.0**np.linspace(0.0, 2.0, 20)
# y = x**2.0
# plt.errorbar(x, y, xerr=0.1*x, yerr=5.0 + 0.75*y)
# ax.set_ylim(ymin=0.1)
# ax.set_title('Errorbars go negative')


# plt.show()
# 
def me_plot(x,y):
	xaxis = [ math.log(_,10) for _ in x]
	yaxis = [ math.log(_,10) for _ in y]
	plt.plot(xaxis, yaxis)
	plt.grid(True)
	plt.title('loglog base 10 on x & y')
	plt.show()
