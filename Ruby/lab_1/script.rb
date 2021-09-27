
def initialise_variable(variable_name)
  print "Enter ", variable_name, ": "
  gets.chomp.to_f
end

def calculate_expression(a, b, c, fi, x)
  top = Math.sin(x)**3 + Math.atan(fi) - 6*10**3.1
  bottom = Math.sqrt(a*x**2 + b*x + c)
  additional = Math.exp(x) * (Math.tan(x+2)).abs
  top / bottom + additional
end

a = initialise_variable"a"
b = initialise_variable"b"
c = initialise_variable"c"
fi = initialise_variable"fi"
x = initialise_variable"x"
puts calculate_expression a,b,c,fi,x

