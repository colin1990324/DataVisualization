# -*- coding: utf-8 -*-

from zhihu import Question
from zhihu import Answer
from zhihu import User
from zhihu import Collection
from collections import defaultdict
import json
from random import randint
import math

def write_file(users, questions, answering, asking):

    with open('network_templete.json', 'r') as f:
        graph = json.load(f)

    graph['nodes'].extend(users)
    graph['nodes'].extend(questions)

    graph['links'].extend(answering)
    graph['links'].extend(asking)

    with open('network.json', 'w') as outfile:
    	json.dump(graph, outfile)

def main():
	# read wanted user url from users.txt
    lines = [line.rstrip('\n') for line in open("users_example.txt")]

    # get (users)
    users = [User(user_url) for user_url in lines]
    user_ids = [user.get_user_id() for user in users]
    for user_id in user_ids:
    	print "user node: " + user_id

    # get (user)-[follow]->(user) relationships
    # here I use followers, since followers are usually fewer than followees
    # following = []
    # for user in users:
    #     print "processing followers of user: " + user.get_user_id()
    #     followers = user.get_followers()
    #     for follower in followers:
    #         if follower.get_user_id() in user_ids:
    #             following.append((follower.get_user_id(), user.get_user_id()))

    # for src, dst in following:
    #     print "follow relationship: " + src + " follows " + dst
    
    # get (user)-[answer]->(question) relationships
    answers = []
    answerings = []
    questions = defaultdict(int)
    for user in users:
        print "processing answers of user: " + user.get_user_id()
        answer = list(user.get_answers())
        answers.extend(answer)
        for a in answer:
    	    answerings.append((user.get_user_id(), a))
            questions[a.get_question().get_title()] += 1

    print "number of answers: " + str(len(answers))
    print "number of answerings: " + str(len(answerings))

    # get (user)-[ask]->(question) relationships
    asking = []
    for user in users:
        print "processing questions of user: " + user.get_user_id()
        asks = list(user.get_asks())
        for ask in asks:
            asking.append((user.get_user_id(), ask.get_title()))
    	    questions[ask.get_title()] += 1

    print "number of asking: " + str(len(asking))
    
    # filter by intervel
    # a question node should have at least two relaionships( answering or asking)
    questions = { k:v for (k,v) in questions.iteritems() if v > 1 }
    for question, num in questions.iteritems():
    	print "question: " + question + " is mentioned " + str(num) + " times."

    # prepare data for writing
    id_map = {}
    index = 0
    usersOut = []
    for user in users:
        id_map[user.get_user_id()] = index
        usero = {}
        usero['id'] = index
        usero['Year'] = index
        usero['cYear'] = index
        usero['Type'] = 'User'
        usero['label'] = user.get_user_id()
        follower_num = user.get_followers_num()
        usero['follower_num'] = follower_num
        if follower_num < 1:
            usero['size'] = 1
        else:
            usero['size'] = math.ceil(math.log(follower_num))
            usersOut.append(usero)
            index += 1

    questionOut = []
    for question, v in questions.iteritems():
        id_map[question] = index
        questiono = {}
        questiono['id'] = index
        questiono['Year'] = randint(1, index)
        questiono['cYear'] = questiono['Year']
        questiono['Type'] = 'Question'
        questiono['label'] = question
        questionOut.append(questiono)
        index += 1

    askOut = []
    for ask in asking:
        if ask[1] in questions:
            asko = {}
            asko['Edge Id'] = str(index)
            asko['target'] = id_map[ask[1]]
            asko['source'] = id_map[ask[0]]
            asko['Year'] = id_map[ask[1]]
            askOut.append(asko)
            index += 1

    answerOut = []
    for answering in answerings:
        title = answering[1].get_question().get_title()
        if title in questions:
            answero = {}
            answero['Edge Id'] = str(index)
            answero['target'] = id_map[title]
            answero['source'] = id_map[answering[0]]
            answero['Year'] = id_map[title]
            answerOut.append(answero)
            index += 1

    write_file(usersOut,questionOut,askOut,answerOut)

def test():
    lines = [line.rstrip('\n') for line in open("users_example.txt")]
    for line in lines:
        u = User(line)
        print u.get_user_id()


if __name__ == '__main__':
    main()
    # validation user_url
    # test()
