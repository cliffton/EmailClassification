def count_lines():
	with open("../data/emails.csv",'r') as f:
		print(len(f.readlines()))



count_lines()