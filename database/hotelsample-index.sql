/* Generated with g9. */
--
-- Run this schema script against Derby as follows:
-- java org.apache.derby.tools.ij hotelsample-index.sql

-- ALTER TABLE and CREATE FOREIGN KEY/INDEX statements

-- Booking
ALTER TABLE Booking ADD CONSTRAINT Booking_customer_customerNoF FOREIGN KEY (customer_customerNo) REFERENCES Customer (customerNo);
ALTER TABLE Booking ADD CONSTRAINT Booking_hotel_idF FOREIGN KEY (hotel_id) REFERENCES Hotel (id);
ALTER TABLE Booking ADD CONSTRAINT Booking_roomCategory_idF FOREIGN KEY (roomCategory_id) REFERENCES RoomCategory (id);

-- Hotel
ALTER TABLE Hotel ADD CONSTRAINT Hotel_chain_idF FOREIGN KEY (chain_id) REFERENCES HotelChain (id);

-- HotelChain

-- HotelRoomCategory
ALTER TABLE HotelRoomCategory ADD CONSTRAINT HotelRoomCategory_hotel_idF FOREIGN KEY (hotel_id) REFERENCES Hotel (id);
ALTER TABLE HotelRoomCategory ADD CONSTRAINT HotelRoomCatego_roomCategory_F FOREIGN KEY (roomCategory_id) REFERENCES RoomCategory (id);

-- Address
ALTER TABLE Address ADD CONSTRAINT Address_customer_customerNoF FOREIGN KEY (customer_customerNo) REFERENCES Customer (customerNo);

-- Room
ALTER TABLE Room ADD CONSTRAINT Room_category_idF FOREIGN KEY (category_id) REFERENCES RoomCategory (id);
ALTER TABLE Room ADD CONSTRAINT Room_hotel_idF FOREIGN KEY (hotel_id) REFERENCES Hotel (id);

-- RoomCategory
