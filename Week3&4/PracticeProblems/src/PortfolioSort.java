import java.util.Random;
class Asset{
    String name;
    double returnRate,volatility;
    Asset(String n,double r,double v){
        name=n;returnRate=r;volatility=v;
    }
}
public class PortfolioSort{
    static void mergeSort(Asset[] a,int l,int r){
        if(l<r){
            int m=(l+r)/2;
            mergeSort(a,l,m);
            mergeSort(a,m+1,r);
            merge(a,l,m,r);
        }
    }
    static void merge(Asset[] a,int l,int m,int r){
        int n1=m-l+1,n2=r-m;
        Asset[] L=new Asset[n1],R=new Asset[n2];
        for(int i=0;i<n1;i++)L[i]=a[l+i];
        for(int j=0;j<n2;j++)R[j]=a[m+1+j];
        int i=0,j=0,k=l;
        while(i<n1&&j<n2){
            if(L[i].returnRate<=R[j].returnRate)a[k++]=L[i++];
            else a[k++]=R[j++];
        }
        while(i<n1)a[k++]=L[i++];
        while(j<n2)a[k++]=R[j++];
    }
    static void quickSort(Asset[] a,int l,int h){
        if(h-l<=10){
            insertion(a,l,h);
            return;
        }
        if(l<h){
            int p=partition(a,l,h);
            quickSort(a,l,p-1);
            quickSort(a,p+1,h);
        }
    }
    static int partition(Asset[] a,int l,int h){
        int p=median3(a,l,h);
        swap(a,p,h);
        double pr=a[h].returnRate,pv=a[h].volatility;
        int i=l-1;
        for(int j=l;j<h;j++){
            if(a[j].returnRate>pr||(a[j].returnRate==pr&&a[j].volatility<pv)){
                i++;swap(a,i,j);
            }
        }
        swap(a,i+1,h);
        return i+1;
    }
    static int median3(Asset[] a,int l,int h){
        int m=(l+h)/2;
        if(a[l].returnRate>a[m].returnRate)swap(a,l,m);
        if(a[l].returnRate>a[h].returnRate)swap(a,l,h);
        if(a[m].returnRate>a[h].returnRate)swap(a,m,h);
        return m;
    }
    static void insertion(Asset[] a,int l,int h){
        for(int i=l+1;i<=h;i++){
            Asset key=a[i];
            int j=i-1;
            while(j>=l&&(a[j].returnRate<key.returnRate||(a[j].returnRate==key.returnRate&&a[j].volatility>key.volatility))){
                a[j+1]=a[j];j--;
            }
            a[j+1]=key;
        }
    }
    static void swap(Asset[] a,int i,int j){
        Asset t=a[i];a[i]=a[j];a[j]=t;
    }
    public static void main(String[] args){
        Asset[] a={
            new Asset("AAPL",12,5),
            new Asset("TSLA",8,7),
            new Asset("GOOG",15,4)
        };
        mergeSort(a,0,a.length-1);
        quickSort(a,0,a.length-1);
    }
}

