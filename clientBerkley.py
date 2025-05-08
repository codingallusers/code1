# client.py
import socket           # To handle networking and communication
import datetime         # To work with time-related data
import time             # To manage time-related functions
import pickle          # To serialize and deserialize data for sending over the network
import random          # To simulate random variations in the client's clock

def start_client(host='localhost', port=8080):
    client = socket.socket()                   # Create a socket object for the client
    client.connect((host, port))               # Connect to the server at given host and port

    # Simulate clock drift by adding a random number of seconds to the current time
    local_time = datetime.datetime.now() + datetime.timedelta(seconds=random.randint(-30, 30))
    print(f"[CLIENT] Local clock before sync: {local_time}")

    # Serialize and send the local time to the server for comparison
    client.send(pickle.dumps(local_time))  # pickle.dumps converts the object to byte format

    # Receive adjustment value from server (the server will calculate and send this)
    adjustment = pickle.loads(client.recv(1024))  # pickle.loads deserializes the received byte data
    print(f"[CLIENT] Received adjustment: {adjustment:.2f} seconds")

    # Adjust the local time based on the received adjustment to synchronize with the server
    synced_time = local_time + datetime.timedelta(seconds=adjustment)
    print(f"[CLIENT] Synchronized clock: {synced_time}")

if __name__ == "__main__":
    start_client()  # Start the client when the script is run
