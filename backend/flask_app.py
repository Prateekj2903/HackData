#!/usr/bin/python
# -*- coding: utf-8 -*-
from flask import Flask, request, jsonify, send_file
import base64
import json
import os
import keras
import numpy as np
from keras.models import load_model
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Conv2D, MaxPooling2D, Flatten
# from PIL import Image
from pyfcm import FCMNotification
import matplotlib.pyplot as plt
import cv2
import hackr
app = Flask(__name__)
APP_ROOT = os.path.dirname(os.path.abspath(__file__))
val=""
i=0
@app.route('/')
@app.route('/image', methods=['GET', 'POST'])
def imageFunction():
    if request.method == 'GET':
        return "getting data"
    elif request.method == 'POST':
        #data=request.form.getlist('[]')
        data=request.get_json(force=True)
        newFile=data['imageFile']
        i = newFile[0]
        newFile = newFile[1:]
        # print(newFile)
        # newFile=data[10:]
        # newFile= newFile.replace('%2F','/')
        # newFile = newFile.replace('%0A','')
        # newFile = newFile.replace('%2B','+')
        # newFile = newFile.replace('%3D&','=')
        # print(newFile)

        imgdata = base64.b64decode(newFile)
        
        random_digits = hackr.generator.digits(10)
        random_chars = hackr.generator.chars(10)
        
        filename = 'some_image.png'
        with open(filename, 'wb') as f:
            f.write(imgdata)

        img = cv2.imread('some_image.png', 0)
        
        # plt.imshow(img, cmap='gray')
        



        if i == 0:
            img = cv2.resize(img, (28, 28))
            resized = img.reshape((1,28,28,1))
            model = Sequential()
            model.add(Conv2D(64, kernel_size=(3, 3), input_shape = (28, 28, 1)))
            model.add(Activation('relu'))
            model.add(MaxPooling2D(pool_size=(2, 2)))
            model.add(Dropout(0.25))
            model.add(Conv2D(64, kernel_size=(3, 3)))
            model.add(Activation('relu'))
            model.add(Flatten())
            model.add(Dense(256))
            model.add(Activation('relu'))
            model.add(Dropout(0.25))
            model.add(Dense(10))
            model.add(Activation('softmax'))
            # model.summary()

            model.load_weights('weights.h5')

            pred = model.predict(resized)
        # print (pred)
            val=np.argmax(pred)
            
            if val in random_digits:
                print val
        # print (val)
        # print ("Got Image")
        # plt.show()

        else:
            img = cv2.resize(img, (64, 64))
            resized = img.reshape((1,64,64,1))
            model = load_model('model-chars.h5')

            pred = model.predict(resized)
            val = np.argmax(pred)
            if i in random_chars:
                print val    
                    
        testNotif()
        return "Success"

def testNotif():
    push_service = FCMNotification(api_key="AAAAoroxYPI:APA91bHpnCDdaqNnCPLDPJ83v_RypNglyzBZmplMn_Qay_XCYBlCDUUTgG9pxwKL0xmgUA2GO1i-BZzBVUZ93X6Dj7cCHW7V-QfRigJRN7ZBgO9sphDW6czbc_rHVL0M8Z3RURcrH-Qi")
    registration_id = "ea8cVELQ8m8:APA91bFHNItw5DrXPJ3us7RST_fl_Sv38nGA4Q0YF0d1ryn5j3U8mk6rxEKR7F3ux9sd3-cYdF29gBRGHeNopM24KYKAx_wVkeXJNjzqbHh7wlgGLbqTRWml9l7Om6aYDWRd9y8WH_xc"
    data_message = {
        "ans":val, 
        "type":i
        }
    result = push_service.notify_single_device(registration_id=registration_id, data_message=data_message)
    print result
    return "success"

if __name__ == '__main__':
    app.debug = False
    app.run(host='0.0.0.0', port=8079)  

