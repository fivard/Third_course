matrix = (0..7).map { Array.new(8, 1) }

def output_matrix(matrix)
  puts
  matrix.each_index do |i|
    matrix[i].each_index do |j|
      print matrix[i][j], "\t"
    end
    puts
  end
end

def mult_matrix_and_number(matrix, number)
  matrix.each_index do |i|
    matrix[i].each_index do |j|
      matrix[i][j] = matrix[i][j] * number
    end
  end
end

def generate_matrix(matrix)
  matrix.each_index do |i|
    matrix[i].each_index do |j|
      if i != j
        matrix[i][j] = rand 10
      end
    end
  end
  matrix
end

matrix = generate_matrix matrix
output_matrix matrix
mult_matrix_and_number(matrix, 2)
output_matrix matrix
