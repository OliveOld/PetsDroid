package olive.Pets.Activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class Transit<T>
        implements View.OnClickListener
{
    Activity from;
    T to;

    public Transit(Activity _from)
    {
        from = _from;
    }

    @Override
    public void onClick(View v)
    {
        try{
            Intent intent = new Intent(from, to.getClass());
            if(intent != null){
                from.startActivity(intent);
            }
            else{
                Log.e("Transit", from.toString() + " -> " + to.toString());
            }
        }
        catch(Exception exc){
            Log.e("Transit", exc.getMessage());
        }
    }
}
