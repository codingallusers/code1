# server.py

import socket                # For network communication
import threading             # To handle multiple clients in parallel
import datetime              # To get the current server time
import time                  # For delays
import random                # (Optional here, can be used for simulation)
import pickle                # To serialize and deserialize Python objects

# Dictionaries to store time offsets and client connections
client_offsets = {}          # {client_address: time_offset}
client_clocks = {}           # {client_address: connection_socket}

# Function to handle each connected client
def handle_client(conn, addr):
    try:
        # Receive client's clock time and deserialize it
        client_time = pickle.loads(conn.recv(1024))
        print(f"[{addr}] Clock received: {client_time}")
        
        # Get server's current time
        current_time = datetime.datetime.now()

        # Calculate time offset (client_time - server_time)
        offset = (client_time - current_time).total_seconds()

        # Save the offset and connection for synchronization
        client_offsets[addr] = offset
        client_clocks[addr] = conn

    except Exception as e:
        print(f"Error with client {addr}: {e}")

# Function that periodically synchronizes all connected clients
def sync_clocks():
    while True:
        if client_offsets:
            # Calculate average time offset across all clients
            avg_offset = sum(client_offsets.values()) / len(client_offsets)
            print(f"\n[SYNC] Avg Offset: {avg_offset:.2f} seconds")

            # Send each client an adjustment = avg_offset - their_offset
            for addr, conn in client_clocks.items():
                try:
                    adjustment = avg_offset - client_offsets[addr]
                    conn.send(pickle.dumps(adjustment))  # Send adjustment
                    print(f"[SYNC] Sent adjustment {adjustment:.2f} sec to {addr}")
                except:
                    print(f"[ERROR] Failed to send to {addr}")
        else:
            print("[SYNC] No clients connected.")

        # Wait 10 seconds before syncing again
        time.sleep(10)

# Function to start the server
def start_server(host='localhost', port=8080):
    server = socket.socket()              # Create socket object
    server.bind((host, port))             # Bind to host and port
    server.listen(5)                      # Listen for up to 5 connections
    print(f"[STARTED] Clock server running on {host}:{port}")

    # Start background thread to handle clock syncing
    threading.Thread(target=sync_clocks, daemon=True).start()

    # Continuously accept client connections
    while True:
        conn, addr = server.accept()      # Accept new client
        threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
        # Handle each client in its own thread

# Run the server when this script is executed
if __name__ == "__main__":
    start_server()
