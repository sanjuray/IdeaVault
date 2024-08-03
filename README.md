Implemented User login/SignUp & SignOut using Google Login API
Saved the user email and name in Shared preferences

Used two tables in SQLite for users and notes tables one for storing existing users and 
one for storing notes

Notes are stored in table in the schema: Id, title,content, last edited time & mailId

RecyclerView has an empty state and non-empty state where on each shows different layout.

Add Note Fragment has edited time, and delete only non-empty notes and same is true for creation.
