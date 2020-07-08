from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy 
import aiml
import random
import requests

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///corpus.db'
db = SQLAlchemy(app)
developer = 0

# Create table description
class Courses(db.Model):
    Code = db.Column(db.String(50), primary_key=True)
    Title = db.Column(db.String(50), nullable=False)
    Domain = db.Column(db.String(50), nullable=False)
    Faculty = db.Column(db.String(50), nullable=False)

    def __repr__(self):
        return '<Course %r>' % self.id


def githubSearch(domain):
    github_response = requests.get('https://jobs.github.com/positions.json?description={}&page=0'.format(domain))
    github_json = github_response.json()
    num = [len(github_json)]
    if num[-1]==50:
        i=1
        while(num[-1]==50):
            github_response = requests.get('https://jobs.github.com/positions.json?description={}&page={}'.format(domain, i))
            github_json = github_response.json()
            num.append(len(github_json))
            i+=1
    return sum(num)

random_responses_greet = ["Okay. Not that you asked, but I am doing pretty well myself :) \nDo you need help in choosing a course?",
                    "That's great. Can I help you in choosing an elective for next semester?",
                    "Okay, may I assist you in choosing a course for next semester"]

random_responses_faculty = ["Awesome! Who?", "That makes my job easier. Who is it?", "Okay, we're making progress. Who is it?"]

# Endpoint to retrieve text message
@app.route('/', methods=['POST'])
def index():

    persistent = open("persistent.txt", "a")

    req_data = request.get_json()
    string = req_data['message']
    persistent.write(string + '\n')
    response  = kernel.respond(string) # Retrieve response from AIML session
    
    # Random reponse for faculty
    if response == 'FACULTY':
        response_string = random.choice(random_responses_faculty)
        persistent.write(response_string + '\n')
        return jsonify(message=response_string)
    
    # Random reponse for how are you
    if response == 'GREET':
        response_string = random.choice(random_responses_greet)
        persistent.write(response_string + '\n')
        return jsonify(message=response_string)


    # Reponse if domain is specified
    if response[0] == '@':
        courses = Courses.query.filter_by(Domain=response[2:])
        courses_string = "These are the courses I found which may be related to {}\n".format(response[2:])
        i=0
        for course in courses:
            i+=1
            courses_string = courses_string + "\n" + course.Title

        if(i!=0):
            courses_string += '\n \n Well, I hope I was helpful. See you next time!'
        else:
            courses_string = "Umm..I do not have courses related to that domain"

        persistent.write(courses_string + '\n')
        return jsonify(message=courses_string)
    
    # Response if faculty is specified
    elif response[0] == '$':
        courses = Courses.query.filter_by(Faculty=response[2:])
        i=0
        courses_string = "These are the courses that Prof. {} will be taking\n".format(response[2:])
        for course in courses:
            i+=1
            courses_string = courses_string + "\n" + course.Title
        print(i)
        if(i!=0):
            courses_string += '\n\nWell, I hope I was helpful. See you next time!'
        else:
            courses_string = "I'm afraid I do not have the list of courses taken by that professor"

        persistent.write(courses_string + '\n')
        return jsonify(message=courses_string)

    elif response == 'DOMAIN':
        response_string = """Are you interested in any of the following domains?
        Software Development
        Big Data
        Computer Architecture
        Theoretical Computer Science"""
        
        persistent.write(response_string + '\n')
        return jsonify(message=response_string)

    # API Call
    elif response == 'API CALL':

        if developer==0:
            response_string = "Sorry, looks like the GitHub Jobs server is not available at this time. Please come back to me later."

        else:
            response_string = "According to GitHub Jobs, software development is trending now with at least {} openings. Are you interested in this field?".format(developer)

        return jsonify(message=response_string)

    else:
        persistent.write(response + '\n')
        return jsonify(message=response)

if __name__ == "__main__":

    try:
        developer = githubSearch('developer')
    except:
        developer = 0
    
    kernel = aiml.Kernel()
    kernel.learn("std-startup.xml")
    
    kernel.respond("load aiml b")
    app.run(host='0.0.0.0', debug=True)