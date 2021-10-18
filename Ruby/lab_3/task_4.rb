$eps = 0.00001
x = 10.0

def factorial(n)
  n > 1 ? n * factorial(n - 1) : 1
end

def calculate_first
  n = 2
  prev_answer = -1
  new_answer = 0
  while (new_answer - prev_answer).abs > $eps
    prev_answer = new_answer
    new_answer += (factorial(n - 1).to_f / factorial(n + 1))**(n*(n + 1))
    n += 1
  end
  new_answer
end

def calculate_second(x)
  n = 0
  answer = 0
  while (2 * answer - Math.log(x, Math.exp(1))).abs > $eps do
    answer += (x - 1)**(2*n+1) / ((2*n + 1)*(x + 1)**(2*n+1))
    n += 1
  end
  answer *= 2
end

def calculate_third
  n = 1
  prev_answer = -1
  new_answer = 0
  while (new_answer - prev_answer).abs > $eps
    prev_answer = new_answer
    new_answer += factorial(4*n - 1).to_f * factorial(2*n - 1) /
                  (factorial(4*n) * 2**(2*n) * factorial(2*n))
    n += 1
  end
  new_answer
end

puts "First = " + calculate_first.to_s
puts "Second = " + calculate_second(x).to_s
puts "Third = " + calculate_third.to_s