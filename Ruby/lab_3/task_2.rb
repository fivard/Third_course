x = 1

if x > -4 && x <= 0
  puts ((x-2).abs / (x**2 * Math.cos(x)))**(1.0/7)
elsif x > 0 && x <= 12
  puts 1 / (Math.tan(x + 1/Math.exp(x)) / Math.sin(x)**2)**(7.0/2)
elsif x < -4 || x > 12
  puts 1 / (1 + x / (1 + x / (1 + x / (1 + x))))
end

case x
when -4..0 then
  if x != -4
    puts ((x-2).abs / (x**2 * Math.cos(x)))**(1.0/7)
  end
when 0..12 then
  if x != 0
    puts 1 / (Math.tan(x + 1/Math.exp(x)) / Math.sin(x)**2)**(7.0/2)
  end
else
  if x != -4
    puts 1 / (1 + x / (1 + x / (1 + x / (1 + x))))
  end
end