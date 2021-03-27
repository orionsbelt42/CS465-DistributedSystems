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

    MSG( String actionKey )
    {
        id = -1;
        name = "Unknown";
        hostName = "";
        port = 0;
        action = actionKey;
        body = "";
        end = true;
    }

    MSG( Node source, String msgAction, String msgBody )
    {
        id = source.id;
        name = source.name;
        hostName = source.hostName;
        port = source.port;
        action = msgAction;
        body = msgBody;
        end = true;
    }

    public String toString()
    {
        String buffer = "";
        if (!action.equals(""))
        {
            buffer += "ACTION: " + action + "\n";
        }


        if (!name.equals(""))
        {
            buffer += "NAME: " + name  + "\n";
        }

        if (id >= 0)
        {
            buffer += "ID: " + id + "\n";
        }

        if (!hostName.equals(""))
        {
            buffer += "HOSTNAME: " + hostName  + "\n";
        }

        if (port >= 0)
        {
            buffer += "PORT: " + port + "\n";
        }

        if (!body.equals(""))
        {
            buffer += "BODY: " + body + "\n";
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
