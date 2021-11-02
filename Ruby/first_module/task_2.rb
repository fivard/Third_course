class House
  attr_accessor :id, :number, :square, :floor, :count_rooms, :street, :building_type, :lifetime

  def initialize(id, number, square, floor, count_rooms, street, building_type="Lok", lifetime=100)
    @id = id
    @number = number
    @square = square
    @floor = floor
    @count_rooms = count_rooms
    @street = street
    @building_type = building_type
    @lifetime = lifetime
  end

  def to_s
    @id.to_s + "_" + @number.to_s + "_" + @square.to_s + "_" + @floor.to_s + "_" + @count_rooms.to_s + \
    "_" + @street + "_" + @building_type + "_" + @lifetime.to_s
  end

end

class House_controller

  def self.filter_rooms(array_of_objects, count_rooms)
    array_of_objects.each do |object|
      if object.count_rooms == count_rooms
        puts object.to_s
      end
    end
  end

  def self.filter_rooms_floor(array_of_objects, count_rooms, start_floor, end_floor)
    array_of_objects.each do |object|
      if object.count_rooms == count_rooms && object.floor >= start_floor && object.floor <= end_floor
        puts object.to_s
      end
    end
  end

  def self.filter_square(array_of_objects, square)
    array_of_objects.each do |object|
      if object.square > square
        puts object.to_s
      end
    end
  end

end

house_1 = House.new(1, 27,123,5,5, "New1", "Lok2", 10)
house_2 = House.new(2, 20,97,1,2, "New2", "Lok5", 20)
house_3 = House.new(3, 12,86,3,3, "New3", "Lok5", 30)
house_4 = House.new(4, 48,23,2,2, "New4", "Lok2", 40)
house_5 = House.new(5, 90,101,4,6, "New6", "Lok1")

array = [house_1, house_2, house_3, house_4, house_5]
puts "Filter by rooms"
House_controller.filter_rooms(array, 5)
puts "\nFilter by rooms and floor"
House_controller.filter_rooms_floor(array, 2, 1, 2)
puts "\nFilter by square"
House_controller.filter_square(array, 80)