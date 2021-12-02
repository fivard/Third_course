class Passenger < ApplicationRecord
  validates :name, presence: true
  validates :count, presence: true
  validates :weight, presence: true
end
