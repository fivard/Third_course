
a = true
b = false
c = true

x = 2
y = 10
z = -50

puts !(a || b) && (a && !b)
puts (z != y ? 1 : 0) <= (6 >= y ? 1 : 0) && a || b && c && x >= 1.5
puts (8 - x * 2 <= z) && (x**2 <= y**2) || (z >= 15)
puts x > 0 && y < 0 || z >= (x * y - y / x) + (-z)
puts !(a || b && !(c || (!a || b)))
puts x**2 + y**2 >= 1 && x >= 0 && y >= 0
puts (a && (c && b != b || a) || c) && b