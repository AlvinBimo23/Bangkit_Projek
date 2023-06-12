# import library
from flask import Flask, request
import json
from flask_restful import Resource, Api, reqparse
from keras_preprocessing import image
import numpy as np
import tensorflow as tf
import tempfile
from tensorflow import keras
from tensorflow.keras.applications.efficientnet import preprocess_input
from werkzeug.datastructures import FileStorage

# flask
app = Flask(__name__)

# flask-restful
api = Api(app)

# ModelLoad
model = keras.models.load_model('chickens_96.42.h5')

with open('Gitt\data.json') as f:
  data = json.load(f)

def classify(img_path):
    class_names=['Coccidiosis', 'Healthy', 'New Castle Disease', 'Salmonella']
    img = image.load_img(img_path, target_size=(150, 150))
    img_array = image.img_to_array(img)

    img_batch = np.expand_dims(img_array, axis=0)

    img_preprocessed = preprocess_input(img_batch)

    pred = model.predict(img_preprocessed)
    score = np.argmax(pred[0])

    # print(score)
    # print("This image most likely belongs to {} with a {:.2f} percent confidence.".format(class_names[score], 100 * np.max(pred[0])))

    return class_names[score]

parser = reqparse.RequestParser()
parser.add_argument('file',
                    type=FileStorage,
                    location='files',
                    required=True,
                    help='provide a file')

class poopClassifier(Resource):
    def get(self):
        response = data
        return response
    
    def post(self):
        args = parser.parse_args()
        the_file = args['file']
        # save a temporary copy of the file
        ofile, ofname = tempfile.mkstemp()
        the_file.save(ofname)
        print(ofname)
        # img = request.files['file']
        # predict
        results = classify(ofname)
        # formatting the results as a JSON-serializable structure:
        output = {}

        output = data[results]

        return output
    
api.add_resource(poopClassifier, '/predict')

if __name__ == '__main__':
    app.run(debug=True)