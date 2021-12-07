class Content
  attr_accessor :notes

  def initialize
    @notes = []
  end

  def add_note(note)
    notes.push(note)
  end

  def find_by_tags(tags)
    notes = []
    @notes.each do |note|
      note.tags.each do |tag|
        if tags.include?(tag)
          notes.push(note)
        end
      end
    end
    notes
  end

  def find_by_date(start_date, end_date)
    notes = []
    @notes.each do |note|
      if note.created_at >= start_date && note.created_at <= end_date
        notes.push(note)
      end
    end
    notes
  end

  def sort_by_updated_at
    @notes.sort_by(&:updated_at)
  end

  def sort_by_header
    @notes.sort_by(&:header)
  end

  def sort_by_importance
    @notes.sort_by(&:importance)
  end
end

class Note
  attr_accessor :tags, :created_at, :updated_at, :importance, :content, :header

  def initialize(tags, importance, content, header)
    @tags = tags
    @created_at = Time.now.utc
    @updated_at = @created_at
    @importance = importance
    @content = content
    @header = header
  end

  def add_tag(tag)
    @tags.push(tag)
    @updated_at = Time.now.utc
  end
end


class Text < Note
  def initialize(tag, importance, content, header)
    super
  end
end

class List < Note
  def initialize(tag, importance, content, header)
    super
  end
end

contents = Content.new
note1 = Text.new(["text", "new"], 1, "text1", "TEXT")
note2 = List.new(["text", "car"], 2, "car", "AAA")
note3 = Text.new(["car"], 1, "blablabla", "Ab")
contents.add_note note1
contents.add_note note2
contents.add_note note3
contents.notes.each { |note| puts note.inspect }
puts "find_by_tags car"
notes1 = contents.find_by_tags(["car"])
notes1.each { |note| puts note.inspect }

puts "add tag lol"
note1.add_tag("lol")
contents.notes.each { |note| puts note.inspect }

puts "sort by importance"
contents.sort_by_header
contents.notes.each { |note| puts note.inspect }