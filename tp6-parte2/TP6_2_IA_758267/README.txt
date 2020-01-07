Para la ejecucion del fichero load_mails.py se debe modificar la ruta de los datasets de Enron (linea 43).

Para la realizacion de los distintos experimentos (pruebas con distintos parametros de Laplace o busqueda del mejor coeficiente de Laplace), hay que modificar la variable 'method' (linea 103) con los valores:
	- 'laplace' para las pruebas con distintos parametrso de Laplace.
	- <otro valor> para la busqueda del mejor parametro de Laplace.

Para la realizacion de las pruebas con las distintas distribuciones, hay que modificar la variable 'learner' de la linea 106, con los valores:
	- 'bernoulli' para el uso de la distribucion Bernoulli.
	- 'multinomial' para el uso de la distribucion Multinomial.
