def f1(x)
  return 1 / ((x + 1) * Math.sqrt(x + 1))
end

def f2(x)
  (Math.tan(x / 2.0 + Math::PI / 4))**3
end

class Calculate
  def method_missing(symbol, x)
    if symbol.to_s == "f1"
      f1(x)
    elsif symbol.to_s == "f2"
      f2(x)
    end
  end
end

def rectangle_method(f, a, b, n)
  h = (b - a) / n.to_f
  sum = 0
  (1..n).each do |i|
    xi = a + i*h - h/2
    if f == "f1"
      sum += f1(xi)
    elsif f == "f2"
      sum += f2(xi)
    end
  end
  h * sum.truncate
end

def trapezoid_method(f, a, b, n)
  h = (b - a) / n.to_f
  sum = 0
  if f == "f1"
    sum += f1(a) / 2 + f1(b) / 2
  elsif f == "f2"
    sum += f2(a) / 2 + f2(b) / 2
  end
  (1..n-1).each do |i|
    xi = a + i*h
    if f == "f1"
      sum += f1(xi)
    elsif f == "f2"
      sum += f2(xi)
    end
  end
  h * sum.truncate
end


print("f1 - rectangle method: ", rectangle_method("f1", 0, 1, 100), "\n")
print("f1 - trapezoid method: ", trapezoid_method("f1", 0, 1, 100), "\n")

print("f2 - rectangle method: ", rectangle_method("f2", 0, 1, 100), "\n")
print("f2 - trapezoid method: ", trapezoid_method("f2", 0, 1, 100), "\n")