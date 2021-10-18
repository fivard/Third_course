x = 2

answer = 1

(3..12).each { |i|
  answer += (-1) ** i * ((i - 1) / i) * x ** (i - 2)
}

puts answer

answer = 1
(1..8).each do |i|
  answer += 1 / 3**i
end

puts answer