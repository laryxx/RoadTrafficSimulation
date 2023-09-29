import os
import sys

from bs4 import BeautifulSoup
from kivy.config import Config
from kivy.uix.behaviors import ButtonBehavior
Config.set('graphics', 'resizable', False)
from kivy.app import App
from kivy.core.window import Window
from kivy.lang import Builder
from kivy.uix.widget import Widget
from kivy_garden.mapview import MapMarker, MapView
from kivy.clock import Clock
import pandas as pd

#Config.set('input', 'mouse', 'mouse,multitouch_on_demand')
Builder.load_file('DisplayResults.kv')
current_frame = 0
my_layout_instance = None
my_mapview_instance = None

def get_car_coordinates_by_frame(frame):
    frames_file_exists = os.path.exists('files\\framesVisual.csv')
    if not frames_file_exists:
        print("Frames output file not found")
        sys.exit(0)
    df = pd.read_csv('files\\framesVisual.csv')
    frame_df = df[df['frame'] == frame]
    coordinates = []
    for index, row in frame_df.iterrows():
        lat = row['lat']
        lon = row['long']
        coordinates.append((lat, lon))
    return coordinates

# def get_car_coordinates_by_frame(frame):
#     frames_file_exists = os.path.exists('files\\framesVisual.csv')
#     if not frames_file_exists:
#         print("Frames output file not found")
#         sys.exit(0)
#     df = pd.read_csv('files\\framesVisual.csv')
#     # print(df)
#     frame_df = df.loc[(df['frame'] == frame), ['lat', 'long', 'type']]
#     # print(len(frame_df))
#     #print(df.iloc[2])
#     # print(df.iloc[2][0], " ", df.iloc[2][1])
#     coordinates = []
#     for i in range(len(frame_df)):
#         print("Append: ", df.iloc[i][0], " ", df.iloc[i][1])
#         coordinates.append((df.iloc[i][0], df.iloc[i][1]))
#     # print(len(coordinates))
#     # print(coordinates[5])
#     # print(coordinates[5][0], " ", coordinates[5][1])
#     #print(frame_df)
#     return coordinates

# TODO
def get_bbox():
    osm_file_exists = os.path.exists('files\\schema.osm')
    if not osm_file_exists:
        print("Map file not found")
        sys.exit(0)
    soup = BeautifulSoup(open('files\\schema.osm', encoding='utf8'), 'html.parser')
    text = soup.find('bounds')['minlat']
    # print("TEXT: " + text)
    minlat = float(soup.find('bounds')['minlat'])
    maxlat = float(soup.find('bounds')['maxlat'])
    minlon = float(soup.find('bounds')['minlon'])
    maxlon = float(soup.find('bounds')['maxlon'])
    # print("MINLAT: ", minlat)
    # print("MAXLAT: ", maxlat)
    # print("MINLON: ", minlon)
    # print("MAXLON: ", maxlon)
    bound_box = (minlat, maxlat, minlon, maxlon)
    print(bound_box)
    return bound_box


def calculate_rectangle_center(min_lat, max_lat, min_long, max_long):
    center_lat = (min_lat + max_lat) / 2
    center_long = (min_long + max_long) / 2
    return center_lat, center_long


class MyLayout(Widget):
    def __init__(self, **kwargs):
        super(MyLayout, self).__init__(**kwargs)
        global my_layout_instance
        my_layout_instance = self
        # self.ids.map_new_center_on_coordinates_latitude.text = str(51.98612464170503)
        # self.ids.map_new_center_on_coordinates_longitude.text = str(17.776237927246086)
        Clock.schedule_once(self.simulate_button_press, 5)

    def center_on(self, lat, long):
        mapview = self.ids.mapview
        mapview.center_on(lat, long)

    def simulate_button_press(self, dt):
        self.restart_ui()
        print("====UI Loaded====")


    def restart_ui(self):
        # lat = str(self.ids.map_new_center_on_coordinates_latitude.text)
        # long = str(self.ids.map_new_center_on_coordinates_longitude.text)

        bbox = get_bbox()
        lat, long = calculate_rectangle_center(bbox[0], bbox[1], bbox[2], bbox[3])
        # lat = 51.98612464170503
        # long = 17.776237927246086\
        print(lat, " ", long)
        if is_float(lat) and is_float(long):
            lat = float(lat)
            long = float(long)
            self.center_on(lat, long)
        else:
            print("Please enter a numeric floating point (e.x. 12.345) value")

    class CustomMapView(ButtonBehavior, MapView):
        def __init__(self, **kwargs):
            super(MyLayout.CustomMapView, self).__init__()
            global my_mapview_instance
            my_mapview_instance = self
            self.markers = []

        def get_bounding_box(self):
            mapview = MapView
            return mapview.get_bbox(self)

        def add_a_marker(self, lat, long):
            mapview = MapView
            marker = MapMarker(lat=lat, lon=long)
            mapview.add_marker(self, marker)
            self.markers.append(marker)

        def remove_all_markers(self):
            for marker in self.markers:
                self.remove_a_marker(marker)

        def remove_a_marker(self, marker):
            mapview = MapView
            mapview.remove_marker(self, marker)

        def show_previous_frame_data(self):
            global current_frame
            self.remove_all_markers()
            previous_frame = current_frame
            frame_number = int(previous_frame) - 100
            current_frame = frame_number
            print(previous_frame, "->", current_frame)
            coordinates = get_car_coordinates_by_frame(int(frame_number))
            print("Frame number: ", frame_number, " cars: ", len(coordinates))
            for i in range(len(coordinates)):
                print(i, ": ", coordinates[i][1], " ", coordinates[i][0])
                lat = float(coordinates[i][1])
                long = float(coordinates[i][0])
                # marker = MapMarker(lat=lat, lon=long)
                # mapview.add_marker(self, marker)
                self.add_a_marker(lat, long)

        def show_next_frame_data(self):
            global current_frame
            self.remove_all_markers()
            previous_frame = current_frame
            frame_number = int(previous_frame) + 100
            current_frame = frame_number
            print(previous_frame, "->", current_frame)
            coordinates = get_car_coordinates_by_frame(int(frame_number))
            print("Frame number: ", frame_number, " cars: ", len(coordinates))
            for i in range(len(coordinates)):
                print(i, ": ", coordinates[i][1], " ", coordinates[i][0])
                lat = float(coordinates[i][1])
                long = float(coordinates[i][0])
                # marker = MapMarker(lat=lat, lon=long)
                # mapview.add_marker(self, marker)
                self.add_a_marker(lat, long)

        def show_frame_data(self):
            global current_frame
            self.remove_all_markers()
            previous_frame = current_frame
            frame_number = self.parent.parent.ids.frame_number_text_input.text
            current_frame = frame_number
            print(previous_frame, "->", current_frame)
            coordinates = get_car_coordinates_by_frame(int(frame_number))
            print("Frame number: " + frame_number, " cars: ", len(coordinates))
            for i in range(len(coordinates)):
                print(i, ": ", coordinates[i][1], " ", coordinates[i][0])
                lat = float(coordinates[i][1])
                long = float(coordinates[i][0])
                # marker = MapMarker(lat=lat, lon=long)
                # mapview.add_marker(self, marker)
                self.add_a_marker(lat, long)

        def on_touch_down(self, touch):
            # print("XXXXXXXX")
            # print(touch[0], touch[1])
            # print(touch)
            # if touch.is_double_tap:
            #     # Ignore double-tap events
            #     return False

            mapview = MapView

            # Checking if the touch event is within the bounds of MapView
            if not mapview.collide_point(self, *touch.pos):
                return False

            # Converting the touch position to map coordinates
            lat, lon = mapview.get_latlon_at(self, touch.pos[0], touch.pos[1])

            # To ignore touches outside the map area
            if lat is None or lon is None:
                return False


            # Printing marker coordinates
            print(f"Lat: {str(lat)}, Long: {str(lon)}")

            #coordinates.clear()
            # coordinates.append((51.98612464170503, 17.776237927246086))
            # coordinates.pop(0)
            # coordinates.pop(1)
            # coordinates.pop(3)
            # coordinates.pop(4)
            # coordinates.pop(5)
            # marker = MapMarker(lat=51.98612464170503, lon=17.776237927246086)
            # mapview.add_marker(self, marker)

            # marker = MapMarker(lat=51.98540210932437, lon=17.804158200000013)
            # mapview.add_marker(self, marker)

            # marker = MapMarker(lat=51.964377424878904, lon=17.82688172492741)
            # mapview.add_marker(self, marker)

            # Updating current new source coordinates
            # self.parent.set_new_source_point_coordinates(str(lat), str(lon))

            # super(MyLayout.CustomMapView, self).set_new_source_point_coordinates(str(lat), str(lon))

            parent_obj = self.parent
            par = mapview.parent
            print(parent_obj)
            # mapview.parent.ids.new_source_point_lat.text = "Lat: " + str(lat)[:8]
            # mapview.parent.ids.new_source_point_long.text = "Long: " + str(lon)[:8]

            # par.current_new_source_point_lat = float(lat)
            # par.current_new_source_point_long = float(lon)

            return True


def is_float(value):
    try:
        float(value)
        return True
    except ValueError:
        return False


class AwesomeApp(App):
    def build(self):
        Window.size = (660, 660)
        Window.clearcolor = (0.7, 0.7, 0.7, 1)
        return MyLayout()


if __name__ == "__main__":
    AwesomeApp().run()