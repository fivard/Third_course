
Array points = [
  [150,49], [221,78], [219,121], [208,158], [167,182],
  [108,192], [72,171], [59,133], [76,115], [91,97],
  [75,78], [67,58], [79,47], [98,30], [118,22],
  [136,30], [143,37]
]

square = 0

points.each_with_index do |value, index|
  next_index = index.next % points.length
  square += (points[index][0] + points[next_index][0])*(points[index][1] - points[next_index][1])
end

puts "Square is ", square.abs / 2
