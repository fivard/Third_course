puts "Enter a"
a = gets.chomp.to_f
puts "Enter b"
b = gets.chomp.to_f
puts "Enter c"
c = gets.chomp.to_f

puts "Enter start_x"
start_x = gets.chomp.to_f
puts "Enter end_x"
end_x = gets.chomp.to_f
puts "Enter dx"
dx = gets.chomp.to_f

answers = {}

(start_x..end_x).step(dx).each do |x|
  if c < 0 && b != 0
    answers[x] = a*x^2 + b*x^2
  elsif c > 0 && b == 0
    answers[x] = (x + a) / (x + c)
  else
    answers[x] = x / c
  end
end

puts "Expression's value: ", (a.truncate & b.truncate) | (a.truncate | c.truncate)

if (a.truncate & b.truncate) | (a.truncate | c.truncate) != 0
  answers.each do |key, value|
    print key, " : ", value, "\n"
  end
else
  answers.each do |key, value|
    print key, " : ", value.to_i, "\n"
  end
end
