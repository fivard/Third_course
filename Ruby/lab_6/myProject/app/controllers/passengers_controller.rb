class PassengersController < ApplicationController
  def query1
    @average = Passenger.sum("weight") / Passenger.sum("count")
    @passengers = Passenger.where("weight / count + 0.3 > %s and weight / count - 0.3 < %s ", @average, @average)
  end

  def query2
    @passengers_1 = Passenger.where("count > 2").count
    @average = Passenger.average("count")
    @passengers_2 = Passenger.where("count > %s", Passenger.average("count")).count
  end

  def query3
    passengers_1 = Passenger.all
    passengers_2 = Passenger.all
    @passengers = []
    passengers_1.each do |passenger_1|
      passengers_2.each do |passenger_2|
        if passenger_1.count == passenger_2.count and passenger_2.id > passenger_1.id
          @passengers.append({"passenger_1": passenger_1, "passenger_2": passenger_2})
        end
      end
    end
  end

  def query4
    @passengers = Passenger.where("count = %s and weight = %s", Passenger.maximum("count"), Passenger.maximum("weight"))
  end

  def query5
    @passengers = Passenger.where("count = 1 and weight < 30.0")
  end

  def index
    @passengers = Passenger.all
  end

  def show
    @passenger = Passenger.find(params[:id])
  end

  def new
    @passenger = Passenger.new
  end

  def create
    @passenger = Passenger.new(passenger_params)

    if @passenger.save
      redirect_to @passenger
    else
      render :new
    end
  end

  def edit
    @passenger = Passenger.find(params[:id])
  end

  def update
    @passenger = Passenger.find(params[:id])

    if @passenger.update(passenger_params)
      redirect_to @passenger
    else
      render :edit
    end
  end

  def destroy
    @passenger = Passenger.find(params[:id])
    @passenger.destroy

    redirect_to root_path
  end

  private
  def passenger_params
    params.require(:passenger).permit(:name, :count, :weight)
  end
end
