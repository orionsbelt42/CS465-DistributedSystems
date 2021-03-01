public class MSG
{
    int id;
    String name;
    String hostName;
    int port;
    String body;
    String action;
    boolean end;

    MSG()
    {
        id = -1;
        name = "";
        hostName = "";
        port = 9090;
        action = "";
        body = "";
        end = true;
    }

    public String toString()
    {
        String buffer = "";
        if (!action.equals(""))
        {
            buffer += "ACTION: " + action;
        }


        if (!name.equals(""))
        {
            buffer += "NAME: " + name;
        }

        if (id >= 0)
        {
            buffer += "ID: " + id + "\n";
        }

        if (!hostName.equals(""))
        {
            buffer += "HOSTNAME: " + hostName;
        }

        if (port >= 0)
        {
            buffer += "PORT: " + port + "\n";
        }

        if (!body.equals(""))
        {
            buffer += "BODY: " + body;
        }

        if (end)
        {
            buffer += "END: TRUE\n";
        }
        else
        {
            buffer += "END: FALSE\n";
        }

        return buffer;

    }
}
