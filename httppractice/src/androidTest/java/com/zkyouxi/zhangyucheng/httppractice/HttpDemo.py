from flask import Flask, jsonify, request

app = Flask(__name__)
app.config["JSON_AS_ASCII"] = False  # jsonify返回的中文正常显示

@app.route("/get",methods=['GET'])
def get():
    return  jsonify({"code": 200, "msg": "GET 返回"})


@app.route("/post", methods=['POST'])
def user_login():
    return  jsonify({"out_trade_no": "ABC", "total_charge": 1234,
        "access_token": "SDZXCGBNX","async_callback_url": "www.askdghjas.com",
        "product_id": "sdffggh","product_name": "ghtrsfdg",
        "product_amount": 12346,"product_desc": "all good",
        "rate": 2,"role_id": "SKTGL123",
        "extend": "透传字段","union_extend": "透传字段SDK专用",
        "currency_code": "RMB","pay_type": 36500,
        "order_no": "SDFKLJGW45672"



        })


if __name__ == '__main__':
    app.run(host='0.0.0.0', port = 5000)