# TodoList
Todo list java application done as part of _VOIS training assingments

### Classes & Responsibilities:
    - TodoManager: is used to manage CRUD operations for Todo items.
    - TodoItem: Data class for Todo items.
    - FileHandler: is used to handle read/write operaitons and parsing data.

### Functionalities:
    - Read/write from/to file:
        - Specify file path in FileHandler's constructor.
        - Call readFromFile() or WriteToFile().
        - readFromFile() will return StringBuilder of file's content.
        - writeToFile() will loop through Map<String, TodoItem> and type toString() into the file as Json Object.

    - CRUD:
        - To use CRUD operations, you have to deal with TodoManager object.
        - insertTodo (TodoItem): inserts todo item into the map.
        - updateTodo (title): takes title 'unique', fetch corresponding item and updates it.
        - deleteTodo (title): takes title 'unique', to specifiy an item and remove it from the map.
        - selectAll(): returns map of todoItems.
        - searchByTitle(title): takes title as parameter and fetch corresponding TodoItem.
        - searchByCategory(category): takes category as param and return map of items that has same category as sent.
        - searchByPariority(pariority): takes pariority enum's selection as param and return map of items that have same pariorities as sent.
        - addToFavorite(title): takes title of an item and toggle its boolean attribute isFavorite.
        - selectNearestByDate(): returns map of 5 elements that have nearest end date.
        - validateInputs(): returns boolean wheather inputs are valid or not.