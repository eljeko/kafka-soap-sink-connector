# AcmeInc UI

# Local test

npm install

export PORT=9090
export ACMERESTAPIURL=http://<HOST>:<PORT>

Local test:
export PORT=9090
export ACMERESTAPIURL=http://localhost:8084


node app.js

# Run image

Build the image:

    ./build-image.sh

Run the image:

    docker run -it -e PORT=9090 -e ACMERESTAPIURL='http://localhost:8084' -p 9090:9090 acme-ui:1.0
