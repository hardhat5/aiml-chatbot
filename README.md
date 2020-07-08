# AIML Chatbot
A chatbot for helping a student decide on an elective course for the upcoming semester.

## Overview
The chatbot is deployed on a Flask server. There is one endpoint for the user to send POST requests. The request is parsed by the AIML library and the approporiate response is returned to the user. The AIML file `basic_chat.aiml` contains the code for conversation. Data for all the courses is stored in an SQLite database. The Android front end sends HTTP POST requests to the server, gets a response and prints it on the screen. 
  