#!/usr/bin/python
# -*- coding: utf-8 -*-
from flask import Flask, request, jsonify, send_file
import base64
import json
import os
import keras
import numpy as np
from pyfcm import FCMNotification
import cv2
import io
from google.cloud import vision
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/Users/aadityasuri/Desktop/apikey.json"

app = Flask(__name__)
APP_ROOT = os.path.dirname(os.path.abspath(__file__))
@app.route('/')
@app.route('/image', methods=['GET', 'POST'])
def imageFunction():
    if request.method == 'GET':
        return "getting data"
    elif request.method == 'POST':
        data=request.get_json(force=True)
        newFile=data['imageFile']
        
        i = newFile[0]
        newFile = newFile[1:]
        
        imgdata = base64.b64decode(newFile)
        

        filename = 'some_image.png'
        with open(filename, 'wb') as f:
            f.write(imgdata)

        img = cv2.imread('some_image.png', 0)
        
        if i == '0':
           val = detect_images(img)
        elif i == '1':
            val = detect_char(img)
        else:
            correct_ans = data['correctAns']
            val = detect_object(img, correct_ans)
                    
        testNotif(i, val)
        return "Success"

def detect_images(img):
    img = cv2.bitwise_not(img)
        
    for j in range(4):
        img = cv2.pyrDown(img)

    img = cv2.pyrDown(img)      
    img = cv2.resize(img, (28,28))
    model = keras.models.load_model('MNIST_Model.h5')
    pred = model.predict_proba(img.reshape(1,28,28,1))
    val=np.argmax(pred)
    print(val)
    return val

def detect_char(img):
    img = cv2.bitwise_not(img)
        
    for j in range(4):
        img = cv2.pyrDown(img)
    
    img = cv2.resize(img, (64, 64))
    model = keras.models.load_model('75k_Model.h5')
    pred = model.predict_proba(img.reshape(1,64,64,1))
    val = np.argmax(pred)
    print(val)
    return val

def detect_object(img, correct_ans):
    vision_client = vision.Client()
    file_name = 'apple.jpg'

    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()
        image = vision_client.image(content=content, )

    labels = image.detect_labels()
    for label in labels:
        print(label.description, label.score)

    for label in labels:
        if str(label.description) == correct_ans:
            return "true"
    
    return "false"

def testNotif(i, val):
    push_service = FCMNotification(api_key="AAAAoroxYPI:APA91bHpnCDdaqNnCPLDPJ83v_RypNglyzBZmplMn_Qay_XCYBlCDUUTgG9pxwKL0xmgUA2GO1i-BZzBVUZ93X6Dj7cCHW7V-QfRigJRN7ZBgO9sphDW6czbc_rHVL0M8Z3RURcrH-Qi")
    registration_id = "ea8cVELQ8m8:APA91bFHNItw5DrXPJ3us7RST_fl_Sv38nGA4Q0YF0d1ryn5j3U8mk6rxEKR7F3ux9sd3-cYdF29gBRGHeNopM24KYKAx_wVkeXJNjzqbHh7wlgGLbqTRWml9l7Om6aYDWRd9y8WH_xc"

    data_message = {
        "ans":val, 
        "type":i
        }
    result = push_service.notify_single_device(registration_id=registration_id, data_message=data_message)
    print(result)
    return "success"

if __name__ == '__main__':
    app.debug = False
    app.run(host='0.0.0.0', port=8079)  

