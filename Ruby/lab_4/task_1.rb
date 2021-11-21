array = Array.new(12) { |i| rand 10}
count = 0
current_number = -10000
array.each_index do |i|
  if i > 0 && array[i] == array[i-1] && current_number != array[i]
    count += 1
    current_number = array[i]
  else
    current_number = -1000
  end
end
p array
puts count