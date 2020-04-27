class Hello:
    __gui = None

    def __init__(self, gui):
        self.__gui = gui

    def run(self):
        print('Hello world!')
        with open("./report.html", "w+") as f:
            f.write("passed")

