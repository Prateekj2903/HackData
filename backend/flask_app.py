#!/usr/bin/python
# -*- coding: utf-8 -*-
#loading the required dependencies
from flask import Flask, request, jsonify, send_file
import base64
import pytesseract
import json
import os
import keras
import numpy as np
from pyfcm import FCMNotification
import cv2
import io
from google.cloud import vision
from PIL import Image
#api key for google cloud vision api
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/Users/aadityasuri/Desktop/apikey.json"

app = Flask(__name__)
APP_ROOT = os.path.dirname(os.path.abspath(__file__))

#defining the api end-point for image handling on server
@app.route('/')
@app.route('/image', methods=['GET', 'POST'])
def imageFunction():
    # GET request to check if the end-point is working
    if request.method == 'GET':
        return "getting data"
    #POST request to recieve image from android app to server
    elif request.method == 'POST':
        # Getting data in the form of JSON with specifies keys
        data=request.get_json(force=True)
        newFile=data['imageFile']
        # the imageFile key in the JSON has the value which is base64 encoded form of image with required option 
        # 0 in the start indicated its a digit image, send the value to i
        # 1 in the start indicates its a character image, send the value to i
        # 2 in the start indicates its object in the image, send the value to i
        i = newFile[0]
        newFile = newFile[1:]
        # Decoding the base64 string ie the image
        imgdata = base64.b64decode(newFile)
        
        # Saving the retrieved image in root directory
        filename = 'some_image.png'
        with open(filename, 'wb') as f:
            f.write(imgdata)
        # Retrieving image from root directory
        img = cv2.imread('some_image.png', 0)
        
        # if i==0 we call the detection of digit function
        if i == '0':
           val = detect_images(img)
        #if i==1 we call detection of character function
        elif i == '1':
            val = detect_char(img)
        #if i==2 or anything else it will call detection of object function
        else:
            correct_ans = data['correctAns']
            val = detect_object(img, correct_ans)
                    
        testNotif(i, val)
        return "{'status':'Success'}"

# This function is used to detect the digit from an image
def detect_images(img):
    #Applying bitwise not operation on image
    img = cv2.bitwise_not(img)
     

    #Scale down the image to the required size   
    for j in range(5):
        img = cv2.pyrDown(img)
      
    img = cv2.resize(img, (28,28))
    
    #Loading the trained model
    model = keras.models.load_model('/Users/aadityasuri/Documents/hackdata/HackData/models/MNIST_Model.h5')
    
    #Predicting the result for the inout image
    pred = model.predict_proba(img.reshape(1,28,28,1))
    val=np.argmax(pred)
    print(val)
    return val

# This function is used to detect the character from an image
def detect_char(img):
    #Applying bitwise not operation on image
    img = cv2.bitwise_not(img)
        
    #Scale down the image to the required size  
    for j in range(5):
        img = cv2.pyrDown(img)

    #Setting up the parameters for tesseract
    kernel = np.ones((1, 1), np.uint8)
    img = cv2.dilate(img, kernel, iterations=1)
    img = cv2.erode(img, kernel, iterations=1)
    tessdata_dir_config = '--tessdata-dir "/usr/local/Cellar/tesseract/3.05.01/share/tessdata/" --psm 10  --oem 2 '
    
    #Applying tesseract on the input image to detect characters 
    arr = Image.fromarray(img)
    result = pytesseract.image_to_string(arr, config = tessdata_dir_config)
    
    print(result)
    return result

# This function detect an object in image
def detect_object(img, correct_ans):
    #Making an object of Google Vision Client
    vision_client = vision.Client()
    file_name = 'some_image.png'

    #Loading the image
    with io.open(file_name, 'rb') as image_file:
        content = image_file.read()
    image = vision_client.image(content=content, )

    #Detecting the labels in image
    labels = image.detect_labels()
    for label in labels:
        print(label.description, label.score)

    for label in labels:
        #if any label description is equal to the correct ans, return true else return false
        if str(label.description) == correct_ans:
            print("true : ",label.description)
            return "true"
    
    return "false"

def testNotif(i, val):
    #Sends notification to the app with calculated result
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

