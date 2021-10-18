$N = 10
$c = 30
$pi = 3.141

def calculate_y(x)
  (x**(1.0/4) - (1/x)**(1.0/4))**(-$N) +
    (x + 1) / (x**2 - 4*x + 3*$N) +
    ((36*x*$c*$N)**(1.0/4) / ((x + $c*$N + 1) / (x + 5)))
end

def calculate_z(x)
  ((Math.tan(2*x) * Math.cos(2*x)**(-1) - Math.tan(2*$c + x) * Math.cos(2*x)**(-2)) /
    (Math.cos(2*x)**(-1) + Math.cos(3*x)**(-2)))
  +
  (1 + (Math.cos($N * x))**(1/$c)) /
    (2*x + Math.sin(3*x)**(2))
end

def output_dict(dict)
  dict.each { |i|
    puts i, dict[i]
  }
end

first_answer = {}
(1..$N).step(($N - 1).to_f / ($N + $c)) do |i|
  first_answer[i] = calculate_y(i)
end


second_answer = {}
i = $pi / $N
delta = ($pi - $pi / $N) / ((3.0/2)*$N + $c)
while i <= $pi do
  second_answer[i] = calculate_z(i)
  i += delta
end


third_answer = {}
(2..$c).step(($c - 2).to_f / (2*$N)) do |j|
  if j > 2 && j < $N
    third_answer[j] = calculate_y(j)
  elsif j > $N && j < 2*$N
    third_answer[j] = calculate_z(j)
  else
    third_answer[j] = calculate_y(j) + calculate_z(j)
  end
end



puts "____________FIRST__________"
output_dict(first_answer)
puts "____________SECOND__________"
output_dict(second_answer)
puts "____________THIRD__________"
output_dict(third_answer)