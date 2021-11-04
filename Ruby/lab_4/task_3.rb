n = 4

def output_matrix(matrix)
  puts
  matrix.each_index do |i|
    matrix[i].each_index do |j|
      print matrix[i][j].round(2), "\t\t"
    end
    puts
  end
  puts
end

def generate_matrix(n)
  matrix = (0..n-1).map { Array.new(n, 2.0) }
  matrix.each_index do |i|
    matrix[i].each_index do |j|
      if i != j
        matrix[i][j] = 3.0 # variant = 1
      end
    end
  end
  matrix
end

def generate_vector(n)
  b = (0..n-1).map{ |i| i + 1 }
end

def gauss(matrix, vector, n)
  x = (0..n-1).map{ Array.new(n) }

  (0..n-1).each do |k|
    # Поиск строки с максимальным a[i][k]
    max = (matrix[k][k]).abs
    index = k
    (k+1..n-1).each do |i|
      if (matrix[i][k]).abs > max
        max = (matrix[i][k]).abs
        index = i
      end
    end
    # Перестановка строк
    (0..n-1).each do |j|
      temp = matrix[k][j]
      matrix[k][j] = matrix[index][j]
      matrix[index][j] = temp
    end
    temp = vector[k]
    vector[k] = vector[index]
    vector[index] = temp
    # Нормализация уравнений
    (k..n-1).each do |i|
      temp = matrix[i][k]
      (0..n-1).each do |j|
        matrix[i][j] = matrix[i][j].to_f / temp
      end

      vector[i] = vector[i] / temp
      if i == k
        next # уравнение не вычитать само из себя
      end
      (0..n-1).each do |j|
        matrix[i][j] = matrix[i][j] - matrix[k][j];
      end
      vector[i] = vector[i] - vector[k]
    end
    output_matrix matrix
  end
  # обратная подстановка
  (n-1).downto(0) do |k|
    x[k] = vector[k].round(2)
    (0..k).each do |i|
      vector[i] = vector[i] - matrix[i][k] * x[k];
    end
  end
  x
end

if n > 9 || n < 3
  puts "n > 9 or n < 3, error"
else
  matrix = generate_matrix n
  vector = generate_vector n
  output_matrix matrix
  p vector
  answer = gauss(matrix, vector, n)
  p answer
end
