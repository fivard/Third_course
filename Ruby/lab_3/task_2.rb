x = 1

if x > -4 && x <= 0
  puts ((x-2).abs / (x**2 * Math.cos(x)))**(1/7)
elsif x > 0 && x <= 12
  puts 1 / (Math.tan(x + 1/Math.exp(x)) / Math.sin(x)**2)**(7/2)
elsif x < -4 || x > 12
  puts 1 / (1 + x / (1 + x / (1 + x / (1 + x))))
end

case x
when -4..0 then
  puts ((x-2).abs / (x**2 * Math.cos(x)))**(1/7)
when 0..12 then
  puts 1 / (Math.tan(x + 1/Math.exp(x)) / Math.sin(x)**2)**(7/2)
else
  puts 1 / (1 + x / (1 + x / (1 + x / (1 + x))))
end