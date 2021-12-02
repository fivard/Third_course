Rails.application.routes.draw do
  root 'passengers#index'

  get "/passengers/query1", to:"passengers#query1", :as => :query1
  get "/passengers/query2", to:"passengers#query2", :as => :query2
  get "/passengers/query3", to:"passengers#query3", :as => :query3
  get "/passengers/query4", to:"passengers#query4", :as => :query4
  get "/passengers/query5", to:"passengers#query5", :as => :query5

  resources :passengers
end
