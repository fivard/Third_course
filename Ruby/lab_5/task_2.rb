def factorial(n)
  n > 1 ? n * factorial(n - 1) : 1
end

def f(x, i)
  Math.log(3)**i * x**i / factorial(i)
end

x = 0.1 # [0.1; 1]
n = 58 # [10; 58]

def calculate_sum(x, n=0)
  sum = 0
  prev_sum = -1
  eps = 0.001
  if n != 0
    (0..n).each do |i|
      sum += f(x, i)
    end
    sum
  else
    i = 0
    while (sum - prev_sum).abs > eps
      prev_sum = sum
      sum += f(x, i)
      i += 1
    end
    sum
  end
end



puts(calculate_sum(x, n))
puts(calculate_sum(x))