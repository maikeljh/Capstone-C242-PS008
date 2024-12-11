import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R

data class Item(
    val name: String,
    val price: String,
    val quantity: Int,
    val total: String
)

data class TransactionItem(
    val title: String,
    val date: String,
    val totalPrice: String,
    val items: List<Item>
)

class ListTransactionAdapter(
    private var transactionList: List<TransactionItem>
) : RecyclerView.Adapter<ListTransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.transactionTitle)
        val date: TextView = itemView.findViewById(R.id.transactionDate)
        val totalAllPrice: TextView = itemView.findViewById(R.id.totalAllPrice)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
//        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
//        val editButton: Button = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.title.text = transaction.title
        holder.date.text = transaction.date
        "+${transaction.totalPrice}".also { holder.totalAllPrice.text = it }
        "Total Harga: ${transaction.totalPrice}".also { holder.totalPrice.text = it }

        holder.itemView.findViewById<LinearLayout>(R.id.itemsContainer).removeAllViews()

        for (item in transaction.items) {
            val itemView = LayoutInflater.from(holder.itemView.context).inflate(
                R.layout.item_list_transaction_item,
                holder.itemView.findViewById(R.id.itemsContainer),
                false
            )

            itemView.findViewById<TextView>(R.id.itemName).text = item.name
            itemView.findViewById<TextView>(R.id.itemPrice).text = item.price
            "${item.quantity}x".also { itemView.findViewById<TextView>(R.id.itemQuantity).text = it }
            itemView.findViewById<TextView>(R.id.itemTotal).text = item.total

            holder.itemView.findViewById<LinearLayout>(R.id.itemsContainer).addView(itemView)
        }
    }

    fun updateData(newTransactionList: List<TransactionItem>) {
        transactionList = newTransactionList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = transactionList.size
}
